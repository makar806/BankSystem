package ru.sinitsyn;

import org.junit.jupiter.api.Test;
import ru.sinitsyn.application.service.DetailService;
import ru.sinitsyn.domain.entities.car.details.Engine.Engine;
import ru.sinitsyn.domain.entities.car.details.Engine.EngineType;
import ru.sinitsyn.infrastructure.repository.InMemoryDetailRepository;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DetailServiceTest {

    @Test
    void create_and_get_detail() {
        InMemoryDetailRepository repo = new InMemoryDetailRepository();
        DetailService service = new DetailService(repo);

        UUID carId = UUID.randomUUID();
        Engine engine = new Engine(UUID.randomUUID(), "1.6 Turbo", 150, 200000, Set.of(carId), EngineType.GAS);

        service.CreateDetail(engine);

        var loaded = service.getDetail(engine.getId());
        assertEquals(engine.getId(), loaded.getId());
        assertEquals("1.6 Turbo", loaded.getName());
    }

    @Test
    void update_price_change_value() {
        InMemoryDetailRepository repo = new InMemoryDetailRepository();
        DetailService service = new DetailService(repo);

        UUID carId = UUID.randomUUID();
        Engine engine = new Engine(UUID.randomUUID(), "engine", 150, 200000, Set.of(carId), EngineType.GAS);
        service.CreateDetail(engine);

        service.updatePrice(engine.getId(), 525252);

        var loaded = service.getDetail(engine.getId());
        assertEquals(525252, loaded.getPriceRub());

    }

    @Test
    void add_compatibility_expands_only() {
        InMemoryDetailRepository repo = new InMemoryDetailRepository();
        DetailService service = new DetailService(repo);

        UUID car1 = UUID.randomUUID();
        UUID car2 = UUID.randomUUID();

        Engine engine = new Engine(UUID.randomUUID(), "engine", 150, 200000, Set.of(car1), EngineType.GAS);
        service.CreateDetail(engine);

        service.addCompatibility(engine.getId(), car2);

        var loaded = service.getDetail(engine.getId());
        assertTrue(loaded.getCompatibleCarIds().contains(car1));
        assertTrue(loaded.getCompatibleCarIds().contains(car2));
    }

    @Test
    void list_details_returns_created() {
        InMemoryDetailRepository repo = new InMemoryDetailRepository();
        DetailService service = new DetailService(repo);

        UUID carId = UUID.randomUUID();
        Engine engine = new Engine(UUID.randomUUID(), "engine", 150, 200000, Set.of(carId), EngineType.GAS);
        service.CreateDetail(engine);

        assertEquals(1, service.listDetails().size());
    }

}
