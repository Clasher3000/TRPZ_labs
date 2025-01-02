-- Створення бази даних (виконувати окремо, якщо базу ще не створено)
CREATE DATABASE myplayer;

-- Створення таблиці playlists
CREATE TABLE IF NOT EXISTS playlists (
                                         id SERIAL PRIMARY KEY,
                                         name TEXT NOT NULL UNIQUE
);

-- Створення таблиці tracks
CREATE TABLE IF NOT EXISTS tracks (
                                      id SERIAL PRIMARY KEY,
                                      playlist_id INTEGER REFERENCES playlists(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    path TEXT NOT NULL,
    position INTEGER
    );
