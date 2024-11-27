create table if not exists game
(
    id          uuid primary key,
    height      int     not null,
    width       int     not null,
    mines_count int     not null,
    completed   boolean not null
);

create table if not exists game_field
(
    id           uuid primary key,
    player_cells text not null,
    hidden_cells text not null,
    foreign key (id) references game (id)
);
