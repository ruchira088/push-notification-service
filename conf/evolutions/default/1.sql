# account schema

# ---!Ups

CREATE TABLE account (
  id VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  airtable_id VARCHAR(100),
  device_token VARCHAR(100) NOT NULL,
  suburb VARCHAR(100),
  state VARCHAR(20) NOT NULL,
  mobile_number VARCHAR(20),
  email VARCHAR(100),

  PRIMARY KEY (id)
)


# ---!Downs

DROP TABLE account