CREATE TABLE IF NOT EXISTS settlement_batches (
  id uuid PRIMARY KEY,
  batch_reference varchar(64) UNIQUE,
  currency char(3),
  psp varchar(128),
  status varchar(32),
  total_amount numeric(18,4),
  item_count integer,
  scheduled_window timestamp with time zone,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  metadata jsonb
);

CREATE TABLE IF NOT EXISTS settlement_items (
  id uuid PRIMARY KEY,
  batch_id uuid,
  payment_id uuid,
  vendor_id varchar(128),
  amount numeric(18,4),
  currency char(3),
  status varchar(32),
  failure_reason text,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_items_payment ON settlement_items(payment_id);
CREATE INDEX IF NOT EXISTS idx_items_status ON settlement_items(status);
