CREATE TABLE chat_room(
    id bigserial primary key,
    title varchar not null unique
);

CREATE TABLE chat_room_message(
    id bigserial primary key,
    message text not null,
    chat_room_id bigint not null,
    sender_id bigint not null,
    send_at timestamp not null,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(id),
    FOREIGN KEY (sender_id) REFERENCES app_user(id)
);

CREATE TABLE chat_room_user(
    chat_room_id bigint not null,
    user_id bigint not null,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(id),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);