package com.peersmarket.marketplace.shared.config;

import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.application.port.out.ImageRepository;
import com.peersmarket.marketplace.item.application.port.out.ItemRepository;
import com.peersmarket.marketplace.item.domain.model.Category;
import com.peersmarket.marketplace.item.domain.model.Image;
import com.peersmarket.marketplace.item.domain.model.Item;
import com.peersmarket.marketplace.item.domain.model.ItemCondition;
import com.peersmarket.marketplace.item.domain.model.ItemStatus;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.application.port.out.CityRepository;
import com.peersmarket.marketplace.review.application.port.out.ReviewRepository;
import com.peersmarket.marketplace.review.application.event.ReviewCreatedEvent; // AJOUT
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.domain.model.AppUserRole;
import com.peersmarket.marketplace.user.domain.model.City;
import com.peersmarket.marketplace.review.domain.model.Review;
import com.peersmarket.marketplace.saveditem.application.port.out.SavedItemRepository;
import com.peersmarket.marketplace.saveditem.domain.model.SavedItem;
import com.peersmarket.marketplace.shared.model.Email;
import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.conversation.application.port.out.ConversationRepository;
import com.peersmarket.marketplace.conversation.domain.model.Conversation;
import com.peersmarket.marketplace.message.application.port.out.MessageRepository;
import com.peersmarket.marketplace.message.domain.model.Message;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher; // AJOUT
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final CityRepository cityRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ReviewRepository reviewRepository;
    private final SavedItemRepository savedItemRepository;
    private final ApplicationEventPublisher eventPublisher; // AJOUT

    public DataInitializer(AppUserRepository appUserRepository,
            CategoryRepository categoryRepository,
            ItemRepository itemRepository,
            ImageRepository imageRepository,
            CityRepository cityRepository,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            ReviewRepository reviewRepository,
            SavedItemRepository savedItemRepository,
            ApplicationEventPublisher eventPublisher) { // AJOUT
        this.appUserRepository = appUserRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.imageRepository = imageRepository;
        this.cityRepository = cityRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.reviewRepository = reviewRepository;
        this.savedItemRepository = savedItemRepository;
        this.eventPublisher = eventPublisher; // AJOUT
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Starting Data Initialization...");

        // 0. Cities
        Optional<City> parisOpt = cityRepository.findByNameIgnoreCase("Paris");
        Optional<City> lyonOpt = cityRepository.findByNameIgnoreCase("Lyon");

        City paris = parisOpt.orElseGet(() -> cityRepository.save(new City("Paris")));
        City lyon = lyonOpt.orElseGet(() -> cityRepository.save(new City("Lyon")));
        if (!parisOpt.isPresent() || !lyonOpt.isPresent()) {
            System.out.println("Cities created or loaded.");
        }

        // 1. Users
        AppUser user1, user2, user3, user4;

        Optional<AppUser> user1Opt = appUserRepository.findByUsername("AliceWonder");
        user1 = user1Opt.orElseGet(() -> {
            AppUser u = new AppUser("AliceWonder", new Email("alice@example.com"), new Password("alicePass123!"),
                    AppUserRole.USER);
            u.setBio("Passionnée de vintage et d'objets uniques.");
            u.setAvatarUrl("https://i.pravatar.cc/150?u=alice");
            u.setVerified(true);
            Address address1 = new Address();
            address1.setCity(paris);
            u.setAddress(address1);
            return appUserRepository.save(u);
        });

        Optional<AppUser> user2Opt = appUserRepository.findByUsername("BobTheBuilder");
        user2 = user2Opt.orElseGet(() -> {
            AppUser u = new AppUser("BobTheBuilder", new Email("bob@example.com"), new Password("bobSecurePass$"),
                    AppUserRole.USER);
            u.setBio("Bricoleur du dimanche, vends outils et créations.");
            u.setAvatarUrl("https://i.pravatar.cc/150?u=bob");
            Address address2 = new Address();
            address2.setCity(paris);
            u.setAddress(address2);
            return appUserRepository.save(u);
        });

        Optional<AppUser> user3Opt = appUserRepository.findByUsername("CharlieChap");
        user3 = user3Opt.orElseGet(() -> {
            AppUser u = new AppUser("CharlieChap", new Email("charlie@example.com"), new Password("charliePwd789"),
                    AppUserRole.USER);
            u.setBio("Collectionneur de chapeaux et accessoires.");
            u.setAvatarUrl("https://i.pravatar.cc/150?u=charlie");
            u.setVerified(true);
            Address address3 = new Address();
            address3.setCity(lyon);
            u.setAddress(address3);
            return appUserRepository.save(u);
        });

        /* Optional<AppUser> user4Opt = appUserRepository.findByUsername("KIHLYouns");
        user4 = user4Opt.orElseGet(() -> {
            AppUser u = new AppUser("KIHLYouns", new Email("kihl@example.com"), new Password("testtest!"),
                    AppUserRole.USER);
            u.setBio("Passionnée de vintage et d'objets uniques.");
            u.setAvatarUrl("https://i.pravatar.cc/150?u=kihl");
            u.setVerified(true);
            Address address1 = new Address();
            address1.setCity(paris);
            u.setAddress(address1);
            return appUserRepository.save(u);
        }); */

        if (!user1Opt.isPresent() || !user2Opt.isPresent() || !user3Opt.isPresent()) {
            System.out.println("Users created or loaded.");
        }

        // 2. Categories
        Category electronics, books, bikes;
        Optional<Category> electronicsOpt = categoryRepository.findByNameIgnoreCase("Électronique");
        electronics = electronicsOpt.orElseGet(() -> categoryRepository.save(new Category("Électronique")));

        Optional<Category> booksOpt = categoryRepository.findByNameIgnoreCase("Livres");
        books = booksOpt.orElseGet(() -> categoryRepository.save(new Category("Livres")));

        Optional<Category> bikesOpt = categoryRepository.findByNameIgnoreCase("Vélos");
        bikes = bikesOpt.orElseGet(() -> categoryRepository.save(new Category("Vélos")));

        if (!electronicsOpt.isPresent() || !booksOpt.isPresent() || !bikesOpt.isPresent()) {
            System.out.println("Categories created or loaded.");
        }

        // 3. Items & Images
        Optional<Item> item1Opt = itemRepository.findByTitleContaining("Super Vélo de Course").stream().findFirst();
        Item item1 = item1Opt.orElseGet(() -> {
            Item i = new Item();
            i.setTitle("Super Vélo de Course");
            i.setDescription("Vélo de course en excellent état, peu servi.");
            i.setPrice(new BigDecimal("250.00"));
            i.setCondition(ItemCondition.USED_LIKE_NEW);
            i.setStatus(ItemStatus.AVAILABLE);
            i.setSeller(user1);
            i.setCategory(bikes);
            i.setCreatedAt(LocalDateTime.now().minusDays(5));
            i.setViewCount(3); // Assurer l'initialisation
            Item savedItem = itemRepository.save(i);
            imageRepository
                    .save(new Image("https://placehold.co/600x400/FF0000/FFFFFF?text=Velo1_1.png", savedItem.getId()));
            imageRepository
                    .save(new Image("https://placehold.co/600x400/FF0000/FFFFFF?text=Velo1_2.png", savedItem.getId()));
            return savedItem;
        });

        Optional<Item> item2Opt = itemRepository.findByTitleContaining("Roman 'Le Seigneur des Anneaux'").stream()
                .findFirst();
        Item item2 = item2Opt.orElseGet(() -> {
            Item i = new Item();
            i.setTitle("Roman 'Le Seigneur des Anneaux'");
            i.setDescription("Trilogie complète, édition de collection.");
            i.setPrice(new BigDecimal("45.50"));
            i.setCondition(ItemCondition.USED_GOOD);
            i.setStatus(ItemStatus.AVAILABLE);
            i.setSeller(user2);
            i.setCategory(books);
            i.setCreatedAt(LocalDateTime.now().minusDays(10));
            i.setViewCount(0); // Assurer l'initialisation
            Item savedItem = itemRepository.save(i);
            imageRepository
                    .save(new Image("https://placehold.co/600x400/00FF00/FFFFFF?text=Livre1.png", savedItem.getId()));
            return savedItem;
        });

        Optional<Item> item3Opt = itemRepository.findByTitleContaining("Casque Audio Bluetooth XYZ").stream()
                .findFirst();
        Item item3 = item3Opt.orElseGet(() -> {
            Item i = new Item();
            i.setTitle("Casque Audio Bluetooth XYZ");
            i.setDescription("Son immersif, autonomie 20h, quasi neuf.");
            i.setPrice(new BigDecimal("75.00"));
            i.setCondition(ItemCondition.USED_LIKE_NEW);
            i.setStatus(ItemStatus.SOLD);
            i.setSeller(user1);
            i.setCategory(electronics);
            i.setCreatedAt(LocalDateTime.now().minusDays(2));
            i.setViewCount(0); // Assurer l'initialisation
            Item savedItem = itemRepository.save(i);
            imageRepository
                    .save(new Image("https://placehold.co/600x400/0000FF/FFFFFF?text=Casque1.png", savedItem.getId()));
            return savedItem;
        });

        Optional<Item> item4Opt = itemRepository.findByTitleContaining("VTT Adulte Nakamura").stream().findFirst();
        Item item4 = item4Opt.orElseGet(() -> {
            Item i = new Item();
            i.setTitle("VTT Adulte Nakamura");
            i.setDescription("VTT robuste pour sentiers, quelques éraflures mais fonctionnel.");
            i.setPrice(new BigDecimal("120.00"));
            i.setCondition(ItemCondition.USED_FAIR);
            i.setStatus(ItemStatus.AVAILABLE);
            i.setSeller(user3);
            i.setCategory(bikes);
            i.setCreatedAt(LocalDateTime.now().minusDays(1));
            i.setViewCount(0); // Assurer l'initialisation
            Item savedItem = itemRepository.save(i);
            imageRepository
                    .save(new Image("https://placehold.co/600x400/FFFF00/000000?text=VTT1.png", savedItem.getId()));
            return savedItem;
        });
        if (!item1Opt.isPresent() || !item2Opt.isPresent() || !item3Opt.isPresent() || !item4Opt.isPresent()) {
            System.out.println("Items and Images created or loaded.");
        }

        // 4. Conversations & Messages
        Optional<Conversation> conv1Opt = conversationRepository.findByItemIdAndUserIds(item1.getId(), user2.getId(),
                user1.getId());
        if (conv1Opt.isEmpty()) {
            conv1Opt = conversationRepository.findByItemIdAndUserIds(item1.getId(), user1.getId(), user2.getId());
        }

        if (conv1Opt.isEmpty()) {
            Conversation conv1ToSave = Conversation.builder()
                    .item(item1)
                    .participants(Arrays.asList(user2, user1))
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .updatedAt(LocalDateTime.now().minusDays(2))
                    .build();
            Conversation conv1 = conversationRepository.save(conv1ToSave);

            Message msg1_1 = Message.builder().conversation(conv1).sender(user2)
                    .content("Bonjour, votre vélo est-il toujours disponible ?")
                    .timestamp(LocalDateTime.now().minusDays(3).plusHours(1)).build();
            messageRepository.save(msg1_1);
            Message msg1_2 = Message.builder().conversation(conv1).sender(user1).content("Oui, toujours !")
                    .timestamp(LocalDateTime.now().minusDays(3).plusHours(2)).build();
            messageRepository.save(msg1_2);
            Message msg1_3 = Message.builder().conversation(conv1).sender(user2)
                    .content("Super, possible de le voir ce week-end ?").timestamp(LocalDateTime.now().minusDays(2))
                    .build();
            messageRepository.save(msg1_3);

            conv1.setLastMessageContent(msg1_3.getContent());
            conv1.setLastMessageSender(msg1_3.getSender());
            conv1.setLastMessageTimestamp(msg1_3.getTimestamp());
            conv1.setUpdatedAt(msg1_3.getTimestamp());
            conversationRepository.save(conv1);
            System.out.println("Conversation 1 and Messages created.");
        }

        Optional<Conversation> conv2Opt = conversationRepository.findByItemIdAndUserIds(item2.getId(), user3.getId(),
                user2.getId());
        if (conv2Opt.isEmpty()) {
            conv2Opt = conversationRepository.findByItemIdAndUserIds(item2.getId(), user2.getId(), user3.getId());
        }
        if (conv2Opt.isEmpty()) {
            Conversation conv2ToSave = Conversation.builder()
                    .item(item2)
                    .participants(Arrays.asList(user3, user2))
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now().minusDays(1))
                    .build();
            Conversation conv2 = conversationRepository.save(conv2ToSave);

            Message msg2_1 = Message.builder().conversation(conv2).sender(user3)
                    .content("Salut, le livre m'intéresse. Quel est l'état exact des pages ?")
                    .timestamp(LocalDateTime.now().minusDays(1).plusMinutes(30)).build();
            messageRepository.save(msg2_1);

            conv2.setLastMessageContent(msg2_1.getContent());
            conv2.setLastMessageSender(msg2_1.getSender());
            conv2.setLastMessageTimestamp(msg2_1.getTimestamp());
            conv2.setUpdatedAt(msg2_1.getTimestamp());
            conversationRepository.save(conv2);
            System.out.println("Conversation 2 and Messages created.");
        }

        // 5. Reviews
        List<Review> reviewsFromUser2 = reviewRepository.findByReviewerId(user2.getId());
        boolean reviewExistsUser2ToUser1 = reviewsFromUser2.stream()
                .anyMatch(review -> review.getReviewee().getId().equals(user1.getId()));

        if (!reviewExistsUser2ToUser1) {
            Review review1 = Review.builder().reviewer(user2).reviewee(user1).rating(5)
                    .comment("Vendeuse très sympa, vélo en parfait état !").createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            reviewRepository.save(review1);
            // Publier l'événement pour mettre à jour les stats de user1
            eventPublisher.publishEvent(new ReviewCreatedEvent(
                    this,
                    review1.getId(),
                    review1.getReviewee().getId(),
                    review1.getRating(),
                    review1.getCreatedAt()
            ));
            System.out.println("Review 1 created and event published.");
        }

        List<Review> reviewsFromUser1 = reviewRepository.findByReviewerId(user1.getId());
        boolean reviewExistsUser1ToUser3 = reviewsFromUser1.stream()
                .anyMatch(review -> review.getReviewee().getId().equals(user3.getId()));

        if (!reviewExistsUser1ToUser3) {
            Review review2 = Review.builder().reviewer(user1).reviewee(user3).rating(4)
                    .comment("Acheteur sérieux, transaction rapide.").createdAt(LocalDateTime.now()).build();
            reviewRepository.save(review2);
            // Publier l'événement pour mettre à jour les stats de user3
            eventPublisher.publishEvent(new ReviewCreatedEvent(
                    this,
                    review2.getId(),
                    review2.getReviewee().getId(),
                    review2.getRating(),
                    review2.getCreatedAt()
            ));
            System.out.println("Review 2 created and event published.");
        }

        // 6. SavedItems
        if (user1 != null && item4 != null) {
            savedItemRepository.findByUserIdAndItemId(user1.getId(), item4.getId()).orElseGet(() -> {
                SavedItem saved1 = SavedItem.builder().user(user1).item(item4)
                        .savedAt(LocalDateTime.now().minusHours(5)).build();
                System.out.println("Creating SavedItem 1 for user " + user1.getId() + " and item " + item4.getId());
                return savedItemRepository.save(saved1);
            });
        } else {
            System.out.println("Skipping SavedItem 1 creation due to null user1 or item4.");
        }

        if (user3 != null && item2 != null) {
            savedItemRepository.findByUserIdAndItemId(user3.getId(), item2.getId()).orElseGet(() -> {
                SavedItem saved2 = SavedItem.builder().user(user3).item(item2)
                        .savedAt(LocalDateTime.now().minusHours(2)).build();
                System.out.println("Creating SavedItem 2 for user " + user3.getId() + " and item " + item2.getId());
                return savedItemRepository.save(saved2);
            });
        } else {
            System.out.println("Skipping SavedItem 2 creation due to null user3 or item2.");
        }
        System.out.println("SavedItems created or loaded if they did not exist.");

        System.out.println("Data Initialization finished.");
    }
}