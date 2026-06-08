CREATE TABLE users
(
    id         UUID         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE categories
(
    id          UUID         NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT unique_category UNIQUE (name)
);

CREATE TABLE products
(
    id          UUID           NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(100)   NOT NULL,
    price       NUMERIC(19, 2) NOT NULL,
    category_id UUID,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE inventories
(
    id         UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    name       VARCHAR(255),
    quantity   INTEGER,
    product_id UUID,
    CONSTRAINT pk_inventories PRIMARY KEY (id),
    CONSTRAINT fk_inventories_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT uk_inventories_product UNIQUE (product_id)
);

CREATE TABLE carts
(
    id           UUID      NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP,
    user_id      UUID,
    total_items  INTEGER,
    unique_items INTEGER,
    total_price  NUMERIC(19, 2),
    CONSTRAINT pk_carts PRIMARY KEY (id),
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_carts_user UNIQUE (user_id)
);


CREATE TABLE cart_item
(
    id          UUID      NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    product_id  UUID,
    cart_id     UUID,
    quantity    INTEGER,
    unit_price  NUMERIC(19, 2),
    total_price NUMERIC(19, 2),
    CONSTRAINT pk_cart_item PRIMARY KEY (id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts (id)
);


CREATE TABLE orders
(
    id           UUID        NOT NULL,
    created_at   TIMESTAMP   NOT NULL,
    updated_at   TIMESTAMP,
    total_price  NUMERIC(19, 2),
    order_status VARCHAR(50) NOT NULL,
    user_id      UUID,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_orders_user UNIQUE (user_id)
);


CREATE TABLE order_item
(
    id          UUID      NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    product_id  UUID,
    order_id    UUID,
    quantity    INTEGER,
    price       NUMERIC(19, 2),
    total_price NUMERIC(19, 2),
    CONSTRAINT pk_order_item PRIMARY KEY (id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE invoice
(
    id          UUID      NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    order_id    UUID,
    total_price NUMERIC(19, 2),
    CONSTRAINT pk_invoice PRIMARY KEY (id),
    CONSTRAINT fk_invoice_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT uk_invoice_order UNIQUE (order_id)
);
