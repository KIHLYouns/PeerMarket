package com.peersmarket.marketplace.shared.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.domain.model.AppUserRole;
import com.peersmarket.marketplace.shared.model.Email;
import com.peersmarket.marketplace.shared.model.Password;
import com.peersmarket.marketplace.item.application.port.out.CategoryRepository;
import com.peersmarket.marketplace.item.domain.model.Category;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(AppUserRepository appUserRepository, CategoryRepository categoryRepository) {
        this.appUserRepository = appUserRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer un vendeur si inexistant
        if(!appUserRepository.existsById(1L)) {
            AppUser seller = new AppUser("sellerTest", new Email("seller@example.com"), new Password("password123"), AppUserRole.USER);
            appUserRepository.save(seller);
            System.out.println("Vendeur créé avec l'ID: " + seller.getId());
        }

        // Créer une catégorie si inexistant
        if(!categoryRepository.existsById(1L)) {
            Category category = new Category();
            category.setName("Vélos");
            categoryRepository.save(category);
            System.out.println("Catégorie créée avec l'ID: " + category.getId());
        }
    }
}