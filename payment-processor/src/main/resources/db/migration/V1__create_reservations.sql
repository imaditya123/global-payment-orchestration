-- V1: reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID NOT NULL,
    vendor_id VARCHAR(255) NOT NULL,
    amount NUMERIC(18,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    status VARCHAR(32) NOT NULL,
    transaction_id VARCHAR(255),
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_reservations_payment_id ON reservations (payment_id);

-- trigger to keep updated_at current
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS set_timestamp ON reservations;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON reservations
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();
