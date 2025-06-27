package com.Lino.fakePlayers.entity;

import java.util.*;

public class SkinDatabase {
    private static final List<String[]> skins = new ArrayList<>();
    private static final Random random = new Random();

    static {
        skins.add(new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTU5MDg1NjE4MjU1MywKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIgogICAgfQogIH0KfQ==",
                "signature_placeholder_1"
        });

        skins.add(new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTU5MDg1NjE4MjU1MywKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbGV4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzgzY2VlNWNhNmFmY2RiMTcxMjg1YWE0MmQ5YWFiMDBlNzhlYzlhZjdmMmU5Y2E3YmUyMDJiY2UyNDIwNmQxZjMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "signature_placeholder_2"
        });

        skins.add(new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTU5MDg1NjE4MjU1MywKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJHYW1lciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMDZmODkwNjRjNWEwOTExOTExZDZhNGEwNzk3N2RmZGUzYzRjNDcwNmJhNDNiYjJhZWNjMjQwNmYyZWQyNyIKICAgIH0KICB9Cn0=",
                "signature_placeholder_3"
        });
    }

    public static String[] getRandomSkin() {
        return skins.get(random.nextInt(skins.size()));
    }

    public static void addSkin(String texture, String signature) {
        skins.add(new String[]{texture, signature});
    }
}