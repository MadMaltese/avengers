package com.tableorder.config;

import com.tableorder.entity.*;
import com.tableorder.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @RequiredArgsConstructor
public class DataInitializer {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData(StoreRepository storeRepo, AdminRepository adminRepo,
                               TableInfoRepository tableRepo, CategoryRepository catRepo, MenuRepository menuRepo) {
        return args -> {
            if (storeRepo.count() > 0) return;

            Store store = new Store(); store.setCode("STORE001"); store.setName("테스트 매장");
            store = storeRepo.save(store);

            Admin admin = new Admin(); admin.setStore(store); admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); admin.setFailedAttempts(0);
            adminRepo.save(admin);

            String tablePw = passwordEncoder.encode("1234");
            for (int i = 1; i <= 5; i++) {
                TableInfo t = new TableInfo(); t.setStore(store); t.setTableNo(i); t.setPassword(tablePw);
                tableRepo.save(t);
            }

            Category main = new Category(); main.setStore(store); main.setName("메인"); main.setSortOrder(1);
            main = catRepo.save(main);
            Category side = new Category(); side.setStore(store); side.setName("사이드"); side.setSortOrder(2);
            side = catRepo.save(side);
            Category drink = new Category(); drink.setStore(store); drink.setName("음료"); drink.setSortOrder(3);
            drink = catRepo.save(drink);

            saveMenu(menuRepo, store, main, "치킨", 18000, "바삭한 후라이드 치킨", 1);
            saveMenu(menuRepo, store, main, "피자", 20000, "모짜렐라 치즈 피자", 2);
            saveMenu(menuRepo, store, main, "파스타", 15000, "크림 파스타", 3);
            saveMenu(menuRepo, store, side, "감자튀김", 5000, "바삭한 감자튀김", 1);
            saveMenu(menuRepo, store, side, "샐러드", 7000, "신선한 야채 샐러드", 2);
            saveMenu(menuRepo, store, side, "치즈스틱", 6000, "모짜렐라 치즈스틱", 3);
            saveMenu(menuRepo, store, drink, "콜라", 2000, "코카콜라 500ml", 1);
            saveMenu(menuRepo, store, drink, "사이다", 2000, "칠성사이다 500ml", 2);
            saveMenu(menuRepo, store, drink, "맥주", 5000, "생맥주 500ml", 3);
        };
    }

    private void saveMenu(MenuRepository repo, Store store, Category cat, String name, int price, String desc, int sort) {
        Menu m = new Menu(); m.setStore(store); m.setCategory(cat); m.setName(name); m.setPrice(price); m.setDescription(desc); m.setSortOrder(sort);
        repo.save(m);
    }
}
