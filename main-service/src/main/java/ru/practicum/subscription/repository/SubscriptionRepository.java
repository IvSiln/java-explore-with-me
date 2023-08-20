package ru.practicum.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.SubscriptionType;
import ru.practicum.event.model.Event;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    String EVENTS_BY_CONFIRMED_PARTICIPANT_SUBSCRIBED =
            "SELECT DISTINCT e FROM requests r " +
                    "INNER JOIN events e ON r.event.id = e.id " +
                    "WHERE r.requester.id = ?1 AND r.status = ru.practicum.enums.RequestStatus.CONFIRMED";

    String EVENTS_BY_OWNER_SUBSCRIBED =
            "SELECT DISTINCT e FROM events e " +
                    "WHERE e.initiator.id = ?1 AND e.state = ru.practicum.enums.EventState.PUBLISHED";

    String OWNERS_EVENTS_FROM_ALL_SUBSCRIBED =
            "SELECT DISTINCT e FROM subscriptions s " +
                    "INNER JOIN events e ON s.owner = e.initiator.id " +
                    "WHERE e.state = ru.practicum.enums.EventState.PUBLISHED " +
                    "AND s.subscriber = ?1";

    String EVENTS_FROM_BY_ALL_CONFIRMED_PARTICIPANTS_SUBSCRIBED =
            "SELECT DISTINCT e FROM events e " +
                    "INNER JOIN requests r ON r.event.id = e.id " +
                    "INNER JOIN subscriptions s ON s.owner = r.requester.id " +
                    "WHERE s.subscriber = ?1 AND r.status = ru.practicum.enums.RequestStatus.CONFIRMED";

    String OWNERS_BY_SUBSCRIBER_AND_TYPE =
            " SELECT u FROM users u " +
                    " JOIN subscriptions s ON u.id = s.owner " +
                    " WHERE s.subscriber = ?1 AND s.type = ?2";

    String SUBSCRIBERS_BY_OWNER_AND_TYPE =
            " SELECT u FROM users u " +
                    " JOIN subscriptions s ON u.id = s.subscriber " +
                    " WHERE s.owner = ?1 AND s.type = ?2";

    @Query(OWNERS_BY_SUBSCRIBER_AND_TYPE)
    List<User> getUsersSubscribed(Long user1Id, SubscriptionType type);

    @Query(SUBSCRIBERS_BY_OWNER_AND_TYPE)
    List<User> getSubscribers(Long owner, SubscriptionType type);

    @Query(OWNERS_EVENTS_FROM_ALL_SUBSCRIBED)
    List<Event> getOwnersEventsFromAllSubscribed(Long subscriber);

    @Query(EVENTS_FROM_BY_ALL_CONFIRMED_PARTICIPANTS_SUBSCRIBED)
    List<Event> getParticipantEventsFromAllSubscribed(Long subscriber);

    @Query(EVENTS_BY_CONFIRMED_PARTICIPANT_SUBSCRIBED)
    List<Event> getEventsByParticipant(Long requesterId);

    @Query(EVENTS_BY_OWNER_SUBSCRIBED)
    List<Event> getEventsByOwner(Long initiatorId);

    Optional<Subscription> getBySubscriberAndOwnerAndType(Long subscriber, Long owner, SubscriptionType type);
}
