-- Number of customers
SELECT sum("customer_count") AS 'Total Customers'
FROM (
  SELECT count(DISTINCT "customer") AS "customer_count"
  FROM "sales"
  GROUP BY "date"
);

-- Total sales
SELECT sum(sale_price) AS 'Total Sales' FROM sales;

-- Total items bought
SELECT count(*) AS 'Total Items Bought' FROM sales;

-- Top 10 selling items with counts
SELECT
  manufacturer,
  product_name,
  item_type,
  products.sku,
  units_sold
FROM products
JOIN (
  SELECT
    sku,
    count(sku) AS units_sold
  FROM sales
  GROUP BY sku
  ORDER BY count(sku) DESC
  LIMIT 10
) top_10 ON products.sku = top_10.sku
ORDER BY units_sold DESC;
