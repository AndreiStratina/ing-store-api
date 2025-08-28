Store Management API
  A backend API for managing store products.

🚀 Features
- Basic product management:
  1. Add product
  2. List/search products (paginated)
  3. Get product by ID
  4. Change product price
- H2 Database
- Basic Auth with role-based access:
  1. ADMIN(admin / admin123): create product, change price
  2. USER(user / user123): read-only

📦 API Endpoints
- POST /api/products → add product (ADMIN)
- GET /api/products/{id} → get product by ID (USER/ADMIN)
- GET /api/products?q=name&page=0&size=10 → list/search products (USER/ADMIN)
- PATCH /api/products/{id}/price → change product price (ADMIN)
- 
🧪 Postman Collection
You can quickly test the API by importing the provided [Postman collection](postman/ing%20store.postman_collection.json).

Base URL: `http://localhost:8081`

