CREATE TABLE IF NOT EXISTS audit_events (
id BIGSERIAL PRIMARY KEY,
payment_id UUID,
vendor_id VARCHAR(128),
event_type VARCHAR(100),
correlation_id VARCHAR(100),
payload JSONB,
created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);


CREATE INDEX IF NOT EXISTS idx_audit_payment ON audit_events(payment_id);
CREATE INDEX IF NOT EXISTS idx_audit_vendor ON audit_events(vendor_id);
CREATE INDEX IF NOT EXISTS idx_audit_event_type ON audit_events(event_type);
CREATE INDEX IF NOT EXISTS idx_audit_created_at ON audit_events(created_at);