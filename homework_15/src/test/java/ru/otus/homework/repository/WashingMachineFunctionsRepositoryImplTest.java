package ru.otus.homework.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.WashingMachineFunction;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс WashingMachineFunctionsRepositoryImpl")
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class WashingMachineFunctionsRepositoryImplTest {

    @Autowired
    private WashingMachineFunctionsRepository repository;

    @DisplayName("Получить все")
    @Test
    void getAll() {
        String func = "dsadsad2";
        WashingMachineFunction machineFunction = new WashingMachineFunction();
        machineFunction.setName(func);
        repository.add(machineFunction);
        assertTrue(repository.getAll().size() > 0);
    }

    @DisplayName("Получить по функции")
    @Test
    void getByName() {
        String func = "dsadsad3";
        WashingMachineFunction machineFunction = new WashingMachineFunction();
        machineFunction.setName(func);
        repository.add(machineFunction);
        assertEquals(machineFunction.getName(), repository.getByName(func).get().getName());
    }

    @DisplayName("Добавление")
    @Test
    void add() {
        String func = "dsadsad";
        WashingMachineFunction machineFunction = new WashingMachineFunction();
        machineFunction.setName(func);
        repository.add(machineFunction);
        WashingMachineFunction addWm = null;
        for (WashingMachineFunction wm : repository.getAll()) {
            if (wm.getName().equals(func)) {
                addWm = wm;
            }
        }
        assertNotNull(addWm);
    }
}