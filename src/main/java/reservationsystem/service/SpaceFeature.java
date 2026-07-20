package reservationsystem.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public enum SpaceFeature {
    PROJECTOR(Set.of("projector")),
    WHITEBOARD(Set.of("whiteboard", "interactive whiteboard")),
    COMPUTER(Set.of("computers", "pc"));

    private final Set<String> persistedAliases;

    SpaceFeature(Set<String> persistedAliases) {
        this.persistedAliases = persistedAliases;
    }

    public boolean matches(List<String> persistedFeatures) {
        if (persistedFeatures == null) {
            return false;
        }

        return persistedFeatures.stream()
                .map(SpaceFeature::normalize)
                .anyMatch(persistedAliases::contains);
    }

    private static String normalize(String feature) {
        return feature == null
                ? ""
                : feature.trim().toLowerCase(Locale.ROOT);
    }
}
