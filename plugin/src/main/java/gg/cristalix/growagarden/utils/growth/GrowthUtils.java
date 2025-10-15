package gg.cristalix.growagarden.utils.growth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GrowthUtils {

    public String formatTime(long millis) {
        if (millis <= 0) {
            return "0с";
        }

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            long remainingHours = hours % 24;
            return days + "д " + remainingHours + "ч";
        } else if (hours > 0) {
            long remainingMinutes = minutes % 60;
            return hours + "ч " + remainingMinutes + "м";
        } else if (minutes > 0) {
            long remainingSeconds = seconds % 60;
            return minutes + "м " + remainingSeconds + "с";
        } else {
            return seconds + "с";
        }
    }

    public String formatProgressBar(double progress, int length) {
        int filled = (int) Math.round((progress / 100.0) * length);
        int empty = length - filled;

        StringBuilder bar = new StringBuilder("§a");

        for (int i = 0; i < filled; i++) {
            bar.append("█");
        }

        bar.append("§7");
        for (int i = 0; i < empty; i++) {
            bar.append("░");
        }

        bar.append(" §f").append(String.format("%.1f", progress)).append("%");

        return bar.toString();
    }

    public String getProgressColor(double progress) {
        if (progress >= 100.0) {
            return "§2";
        } else if (progress >= 75.0) {
            return "§a";
        } else if (progress >= 50.0) {
            return "§e";
        } else if (progress >= 25.0) {
            return "§6";
        } else {
            return "§c";
        }
    }

    public String formatMultiplier(double multiplier) {
        double percentage = (multiplier - 1.0) * 100.0;

        if (percentage > 0) {
            return "§a+" + String.format("%.0f", percentage) + "%";
        } else if (percentage < 0) {
            return "§c" + String.format("%.0f", percentage) + "%";
        } else {
            return "§70%";
        }
    }

    public long calculateRealTime(long baseTime, double multiplier) {
        if (multiplier <= 0) {
            return Long.MAX_VALUE;
        }
        return (long) (baseTime / multiplier);
    }
}