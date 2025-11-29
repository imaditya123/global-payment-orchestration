-- V2: ledger table for audit entries
CREATE TABLE IF NOT EXISTS ledger (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID NOT NULL,
    action VARCHAR(50) NOT NULL,
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
