package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.WashingMachine;
import ru.otus.homework.domain.WashingMachineFunction;
import ru.otus.homework.enums.WMColor;
import ru.otus.homework.enums.WMDoorColor;
import ru.otus.homework.enums.WMTypeOfControl;
import ru.otus.homework.repository.WashingMachineFunctionsRepository;
import ru.otus.homework.repository.WashingMachineRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WashingMachineServiceImpl implements WashingMachineService {
    private final WashingMachineFunctionsRepository wmfRepository;
    private final WashingMachineRepository wmRepository;

    public WashingMachineServiceImpl(WashingMachineFunctionsRepository washingMachineFunctionsRepository, WashingMachineRepository washingMachineRepository) {
        this.wmfRepository = washingMachineFunctionsRepository;
        this.wmRepository = washingMachineRepository;
        createWashingMachineDB();
    }

    @Override
    public List<WashingMachine> getAll() {
        return wmRepository.getAll();
    }

    @Override
    public List<WashingMachineFunction> getAllWMFunctions() {
        return wmfRepository.getAll();
    }

    @Override
    public Optional<WashingMachine> getWashingMachineByModel(String model) {
        return wmRepository.getByModel(model);
    }

    @Override
    public List<WashingMachine> getAllModelsByFunction(String name) {
        return wmRepository.getAll().stream().filter(wm -> existWMFunction(wm, name)).toList();
    }

    private boolean existWMFunction(WashingMachine wm, String name) {
        for (WashingMachineFunction wmf : wm.getWmFunctions()) {
            if (wmf.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void createWashingMachineDB() {
        WashingMachineFunction wmf1 = wmfRepository.add(getWashingMachineFunction("Сушилка", "Функция сушки белья\n" +
                "Функция высушивания постиранного белья прямо в барабане машины при помощи потока тёплого воздуха. Облегчает и ускоряет процесс сушки, однако требует больших затрат электроэнергии, а высыхание происходит менее равномерно, чем при обычной сушке на открытом воздухе."));
        WashingMachineFunction wmf2 = wmfRepository.add(getWashingMachineFunction("Стирка паром", "Стирка паром\n" +
                "Как следует из названия, в данном режиме в барабан машинки подаётся не вода, а нагретый пар. Такое воздействие более эффективно, чем обычная стирка в горячей воде, причём как по качеству очистки, так и по антибактериальному эффекту, при этом оно нередко оказывается ещё и более экономичным по расходу воды и электроэнергии. С другой стороны, эта функция заметно сказывается на цене машинки."));
        WashingMachineFunction wmf3 = wmfRepository.add(getWashingMachineFunction("Пузырьковая стирка", "Воздушно-пузырьковая стирка\n" +
                "В таких машинах во время стирки в барабан подаются мелкие пузырьки воздуха. Они дополнительно участвуют в удалении загрязнений, а также снижают трение между вещами и барабаном, обеспечивая лучшую их сохранность (в некоторых моделях машин барабан при такой стирке вообще не вращается). Такие машины можно рекомендовать, если Вам приходится стирать много деликатных тканей. Ключевым условием для эффективности этой функции является мягкая вода — жёсткая «пузырится» значительно хуже."));
        WashingMachineFunction wmf4 = wmfRepository.add(getWashingMachineFunction("LED экран", "LED дисплей\n" +
                "Дисплей типа LED, расположенный на панели управления машинки.\n" +
                "Под LED в данном случае подразумеваются простейшие экраны, способные отображать лишь цифры и ограниченный набор символов (в отличие от более продвинутых TFT — см. соответствующий пункт). С другой стороны, такие экраны обходятся недорого и могут устанавливаться даже в бюджетные модели, а их возможностей вполне достаточно для отображения большинства параметров работы агрегата: выбранной программы, оставшегося времени работы, информации о дополнительных режимах, сообщений о неполадках и т. п. Поэтому большинство современных «стиралок» (в том числе премиум-класса) оснащается именно LED-дисплеями."));
        WashingMachineFunction wmf5 = wmfRepository.add(getWashingMachineFunction("Подсветка барабана", "Подсветка барабана\n" +
                "Данная функция обеспечивает дополнительное освещение внутри барабана, позволяя лучше видеть содержимое и не забывать в машинке мелкие вещи."));
        WashingMachineFunction wmf6 = wmfRepository.add(getWashingMachineFunction("Люк дозагрузки белья", "Люк для дозагрузки белья\n" +
                "Небольшой дополнительный люк, расположенный обычно на основном люке и позволяющий догрузить в барабан бельё, не прерывая программу стирки — хотя её придётся приостановить, но после этого процесс запускается «с того же места». Такая функция может пригодиться как для дозагрузки забытых вещей, так и для других целей: например, после окончания программы предварительной стирки через люк можно добавить в барабан вещи, не требующие предварительной стирки."));
        WashingMachineFunction wmf7 = wmfRepository.add(getWashingMachineFunction("Прямой привод", "Прямой привод двигателя\n" +
                "В отличие от обычных машин, в которых барабан связан с двигателем ременной передачей, в машинах с прямым приводом барабан находится непосредственно на валу двигателя. Это уменьшает габариты и вес, снижает шум и вибрации; кроме того, такая схема считается более надёжной."));
        WashingMachineFunction wmf8 = wmfRepository.add(getWashingMachineFunction("Инверторный двигатель", "Инверторный двигатель\n" +
                "В конструкции инверторных электродвигателей отсутствуют щетки, что дает ряд преимуществ: в частности, такие двигатели более долговечны, чем обычные, не так шумят и имеют более высокий КПД, т.к. меньше энергии тратят на нагрев. Их главным недостатком можно назвать высокую стоимость."));
        WashingMachineFunction wmf9 = wmfRepository.add(getWashingMachineFunction("Таймер окончания стирки", "Таймер окончания стирки\n" +
                "Функция, позволяющая отложить стирку на определенное время — например, загрузить машинку перед уходом на работу и развесить свежепостиранное белье по возвращении. При этом таймер устанавливается не на начало стирки, а на время, когда она должна закончиться, и машинка автоматически выбирает время старта программы, с учетом ее длительности. Например, если выставить таймер на два часа, а выбранная программа занимает полчаса, то стирка включится через полтора часа и закончится четко в заданное время. Благодаря этому можно планировать время с максимальным удобством."));
        WashingMachineFunction wmf10 = wmfRepository.add(getWashingMachineFunction("Интеллектуальная стрирка", "Интеллектуальная стирка\n" +
                "Интеллектуальная (адаптивная) стирка позволяет включать устройство не задавая конкретных параметров. Стиральная машинка сделает все за вас. Так в одних моделях автоматически определяется тип ткани и на основе заложенных программ включается соответствующий режим работы. В других моделях происходит анализ частоты использования того или иного режима пользователем и стиралка подстраивается под ваши предпочтения. В любом случае такое решение позволяет включить стирку не задумываясь — машинка настроит режим самостоятельно."));

        wmRepository.add(getWashingMachine("IWSB5085", WMColor.БЕЛЫЙ, WMDoorColor.БЕЛЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf1, wmf2, wmf3, wmf4)));
        wmRepository.add(getWashingMachine("CMA60Y1010", WMColor.ЧЕРНЫЙ, WMDoorColor.БЕЛЫЙ, WMTypeOfControl.МЕХАНИЧЕСКИЕ_КНОПКИ, List.of(wmf1, wmf2, wmf3, wmf4)));
        wmRepository.add(getWashingMachine("EWSD51031BK", WMColor.СЕРЕБРИСТЫЙ, WMDoorColor.БЕЛЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf1, wmf8, wmf9, wmf10)));
        wmRepository.add(getWashingMachine("WW60J3097JW", WMColor.СЕРЕБРИСТЫЙ, WMDoorColor.ЧЕРНЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf8, wmf9, wmf10)));
        wmRepository.add(getWashingMachine("FH0B8LD6", WMColor.ЧЕРНЫЙ, WMDoorColor.ЧЕРНЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf4, wmf5, wmf6, wmf7)));
        wmRepository.add(getWashingMachine("WAT20441", WMColor.БЕЛЫЙ, WMDoorColor.ПРОЗРАЧНЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf3, wmf5, wmf7, wmf8)));
        wmRepository.add(getWashingMachine("F10B8ND", WMColor.БЕЛЫЙ, WMDoorColor.ЧЕРНЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf2, wmf4, wmf5, wmf10)));
        wmRepository.add(getWashingMachine("WLR245H2", WMColor.СЕРЕБРИСТЫЙ, WMDoorColor.ПРОЗРАЧНЫЙ, WMTypeOfControl.СЕНСОРНОЕ_УПРАВЛЕНИЕ, List.of(wmf1, wmf2, wmf3, wmf4, wmf5, wmf6, wmf7, wmf8)));
        wmRepository.add(getWashingMachine("F10B8ND", WMColor.БЕЛЫЙ, WMDoorColor.БЕЛЫЙ, WMTypeOfControl.МЕХАНИЧЕСКИЕ_КНОПКИ, List.of(wmf1, wmf2, wmf3, wmf4)));
    }

    private WashingMachineFunction getWashingMachineFunction(String name, String description) {
        WashingMachineFunction washingMachineFunction = new WashingMachineFunction();
        washingMachineFunction.setName(name);
        washingMachineFunction.setDescription(description);
        return washingMachineFunction;
    }

    private WashingMachine getWashingMachine(String model, WMColor color, WMDoorColor doorColor, WMTypeOfControl typeOfControl, List<WashingMachineFunction> wmFunctions) {
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setModel(model);
        washingMachine.setColor(color.toString());
        washingMachine.setDoorColor(doorColor.toString());
        washingMachine.setTypeOfControl(typeOfControl.toString());
        washingMachine.setWmFunctions(wmFunctions);
        return washingMachine;
    }
}
