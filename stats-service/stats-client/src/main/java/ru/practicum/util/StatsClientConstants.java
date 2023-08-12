package ru.practicum.util;

public class StatsClientConstants {
    public static final String INCORRECT_TIME_LIMIT_EXCEPTION_INFO =
            "Please check time limit params: start and end shouldn't be null, end should be after start.";
    public static final String START_END_PATH_PART = "?start={start}&end={end}";
    public static final String HIT_ENDPOINT = "/hit";
    public static final String GET_STATS_ENDPOINT = "/stats";
    public static final String URIS_PATH_PART = "&uris=";
    public static final String UNIQUE_PATH_PART = "&unique=";
}
