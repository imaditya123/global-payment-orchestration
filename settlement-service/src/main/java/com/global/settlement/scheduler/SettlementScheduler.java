package com.global.settlement.scheduler;

import com.global.settlement.dto.BatchRequestDto;
import com.global.settlement.entity.SettlementBatchEntity;
import com.global.settlement.service.BatchBuilderService;
import com.global.settlement.service.SettlementExecutorService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Optional;

@Component
public class SettlementScheduler {

    private static final Logger log = LoggerFactory.getLogger(SettlementScheduler.class);

    private final BatchBuilderService batchBuilderService;
    private final SettlementExecutorService executor;

    @Value("${settlement.cutoff-offset-minutes:10}")
    private long cutoffOffsetMinutes;

    public SettlementScheduler(BatchBuilderService batchBuilderService, SettlementExecutorService executor) {
        this.batchBuilderService = batchBuilderService;
        this.executor = executor;
    }

    /**
     * A simple scheduled job that runs every minute to check if a configured window is due.
     * In production, you'd compute windows and run exactly once per window per region with leader election.
     */
    @Scheduled(cron = "0 */1 * * * *") // every minute
    public void runWindows() {
        try {
            LocalTime now = LocalTime.now(ZoneId.systemDefault());
            // For simplicity: check if now matches any of the example windows +/- cutoffOffsetMinutes
            // In real: read windows from config and compute next window times
            String[] windows = {"09:00","12:00","15:00","18:00"};
            for (String w : windows) {
                LocalTime windowTime = LocalTime.parse(w);
                long minsDiff = Duration.between(now, windowTime).toMinutes();
                if (Math.abs(minsDiff) <= cutoffOffsetMinutes) {
                    // schedule run for this window
                    // window timestamp: today at windowTime
                    BatchRequestDto req = new BatchRequestDto();
                    req.setWindow(OffsetDateTime.of(LocalDate.now(), windowTime, ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
                    req.setCurrency("USD"); // in practice iterate currencies & PSPs
                    req.setPsp("example-psp");

                    Optional<SettlementBatchEntity> batchOpt = batchBuilderService.buildBatch(req.getCurrency(), req.getPsp(), req.getWindow());
                    batchOpt.ifPresent(b -> {
                        log.info("Executing batch {}", b.getId());
                        executor.executeBatch(b.getId());
                    });
                }
            }
        } catch (Exception ex) {
            log.error("Error in settlement scheduler", ex);
        }
    }
}
