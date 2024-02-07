CREATE TABLE IF NOT EXISTS notification(
    id bigserial primary key,
    title varchar,
    content text
);

CREATE TABLE IF NOT EXISTS user_notification(
    user_id bigint not null,
    notification_id bigint not null,
    foreign key (user_id) references app_user(id),
    foreign key (notification_id) references notification(id),
    unique (user_id, notification_id)
);