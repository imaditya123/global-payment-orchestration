CREATE TABLE IF NOT EXISTS payments (
  id uuid PRIMARY KEY,
  vendor_id varchar(128),
  amount numeric(18,4),
  currency char(3),
  target_currency char(3),
  status varchar(50),
  retry_count integer DEFAULT 0,
  metadata jsonb,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);
