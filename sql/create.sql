-- Sqlite schema creation.

-- NOTE: the `products` table can be imported directly from
-- `resources/products.txt`

-- After creating the `sales` table, populate it by importing the output file
-- that results from running the Java code.

DROP TABLE IF EXISTS sales;

CREATE TABLE sales (
  "date" TEXT,
  "customer" INTEGER,
  "sku" TEXT,
  "sale_price" REAL,
  "stock" INTEGER,
  "cases_ordered" INTEGER
);
