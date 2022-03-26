create table if not exists subject
(
    id       integer not null primary key,
    password varchar(255),
    username varchar(255),

    unique (username)
);

create table if not exists user_group
(
    id   integer not null primary key,
    name varchar(255),

    unique (name)
);

create table if not exists subject_groups
(
    -- Usually I'd make sure this was named just "subject_id" but due to time
    -- constraints I'm leaving it as is
    subject_entity_id integer not null,
    groups_id         integer not null,
    constraint subject_user_group_pkey primary key (subject_entity_id, groups_id),
    constraint fk_subject foreign key (subject_entity_id) references subject (id),
    constraint fk_user_group foreign key (groups_id) references user_group (id)
);

-- Used to generate IDs sequentially
create sequence hibernate_sequence;