CREATE TABLE roles_permissions (
                                   role_id BIGINT NOT NULL,
                                   permission_id BIGINT NOT NULL,

                                   CONSTRAINT pk_roles_permissions PRIMARY KEY (role_id, permission_id),

                                   CONSTRAINT fk_roles_permissions_role
                                       FOREIGN KEY (role_id)
                                           REFERENCES roles(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_roles_permissions_permission
                                       FOREIGN KEY (permission_id)
                                           REFERENCES permissions(id)
                                           ON DELETE CASCADE
);