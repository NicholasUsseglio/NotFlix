package com.notflix.springVideo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Quick startup runner that fixes rows in `intrattenimento` with empty/NULL discriminator
 * by setting a sensible value based on existing child tables (e.g. film).
 * This is a small, temporary migration helper to avoid Hibernate "Unrecognized discriminator value" errors
 * when the database contains legacy rows without the discriminator column populated.
 */
@Component
public class DiscriminatorFixRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DiscriminatorFixRunner.class);

    private final JdbcTemplate jdbc;

    public DiscriminatorFixRunner(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // Count problematic rows
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM intrattenimento WHERE tipo IS NULL OR tipo = ''",
                    Integer.class);

            if (count != null && count > 0) {
                logger.warn("Found {} intrattenimento rows with NULL/empty discriminator 'tipo' - attempting automatic fix.", count);

                // If there is a film table that references intrattenimento, set tipo='film' for those
                int updatedFilm = jdbc.update(
                        "UPDATE intrattenimento i JOIN film f ON f.id_intrattenimento = i.id "
                        + "SET i.tipo = 'film' WHERE i.tipo IS NULL OR i.tipo = ''");

                logger.info("Updated {} rows setting tipo='film' based on film table.", updatedFilm);

                // If there are any remaining rows still without tipo, set them to a default value (e.g. 'film')
                int updatedRemaining = jdbc.update(
                        "UPDATE intrattenimento SET tipo = 'film' WHERE tipo IS NULL OR tipo = ''");

                logger.info("Updated {} remaining rows setting tipo='film' as fallback.", updatedRemaining);
            }
        } catch (Exception ex) {
            // Don't fail the startup; just log the issue so the developer can inspect DB manually.
            logger.error("Error while attempting to fix discriminator values in 'intrattenimento': {}", ex.getMessage(), ex);
        }
    }
}
