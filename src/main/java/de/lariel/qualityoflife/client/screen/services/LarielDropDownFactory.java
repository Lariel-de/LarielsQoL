package de.lariel.qualityoflife.client.screen.services;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.npc.widget.DropDownWidget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class LarielDropDownFactory {

    public static <T> DropDownWidget<T> create(
            List<T> options,
            T selected,
            Function<T, String> nameGetter,
            Consumer<T> onSelect
    ) {
        DropDownWidget<T> dropdown = new DropDownWidget<>(90, 20);
        dropdown.setOptionConverter(nameGetter);
        dropdown.setOptions(Lists.newArrayList(options), selected);
        dropdown.setOrdered();
        dropdown.setOnSelected(onSelect);

        return dropdown;
    }
}
