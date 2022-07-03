package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.homework.domain.WashingMachine;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс WashingMachineRepositoryImpl")
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class WashingMachineRepositoryImplTest {

    @Autowired
    private WashingMachineRepositoryImpl washingMachineRepositoryImpl;

    @DisplayName("Добавление")
    @Test
    void add() {
        String model = "dsadsad";
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setModel(model);
        washingMachineRepositoryImpl.add(washingMachine);
        WashingMachine addWm = null;
        for (WashingMachine wm : washingMachineRepositoryImpl.getAll()) {
            if (wm.getModel().equals(model)) {
                addWm = wm;
            }
        }
        assertNotNull(addWm);
    }

    @DisplayName("Получить все")
    @Test
    void getAll() {
        String model = "dsadsad2";
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setModel(model);
        washingMachineRepositoryImpl.add(washingMachine);
        assertTrue(washingMachineRepositoryImpl.getAll().size() > 0);
    }

    @DisplayName("Получить по модели")
    @Test
    void getByModel() {
        String model = "dsadsad3";
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setModel(model);
        washingMachineRepositoryImpl.add(washingMachine);
        assertEquals(washingMachine.getModel(), washingMachineRepositoryImpl.getByModel(model).get().getModel());
    }
}