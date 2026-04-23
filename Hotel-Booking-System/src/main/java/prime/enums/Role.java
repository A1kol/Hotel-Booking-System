package prime.enums;

public enum Role {
    ADMIN, MANAGER, USER, BANNED;

    public static boolean isValid(String role) {
        if (role == null) return false;
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
