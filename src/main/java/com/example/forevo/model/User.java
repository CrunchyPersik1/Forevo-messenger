package com.example.forevo.model;

public record User(
        String email,
        String password,
        Profile profile,
        ChatList chats
) {
    public record Profile(
            String nick,
            String username,
            String avatar,
            String theme
    ) {}

    public record ChatList(
            Chat[] chats
    ) {
        public record Chat(
                String id,
                String name,
                String icon,
                String bg,
                Message[] messages
        ) {}
    }

    public record Message(
            String text,
            String type,
            Integer id,
            String quoteText
    ) {}
}