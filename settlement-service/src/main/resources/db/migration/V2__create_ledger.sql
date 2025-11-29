CREATE TABLE IF NOT EXISTS ledger_entries (
  id bigserial PRIMARY KEY,
  payment_id uuid,
  batch_id uuid,
  entry_type varchar(20),
  amount numeric(18,4),
  currency char(3),
  created_at timestamp with time zone DEFAULT now()
);
