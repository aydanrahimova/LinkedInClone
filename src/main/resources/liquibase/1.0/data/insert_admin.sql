INSERT INTO
    users (id,first_name,last_name, email, password, photo_url,title, about, phone, birth_date,status,deactivation_date)
VALUES
    (1,'Admin', 'AdminSurname','admin@example.com','$2a$10$Pqn13NjT2X4VgrJv4nWRauUWvHl2OYsU8h9Ptv.pngfwX7fHRMGpm',null,null, '+994501234567',null,'2000-01-01','ACTIVE',null);
--admin123-password
INSERT INTO user_authority (user_id, authority_id)
VALUES
    (1, (SELECT id FROM authority WHERE name = 'ADMIN'));