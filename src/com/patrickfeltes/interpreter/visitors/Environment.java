package com.patrickfeltes.interpreter.visitors;

import com.patrickfeltes.interpreter.data_types.TiList;
import com.patrickfeltes.interpreter.errors.RuntimeError;
import com.patrickfeltes.interpreter.tokens.Token;

import java.util.*;

public class Environment {

    private final Map<String, Object> values = new HashMap<>();
    private final Set<String> numberVariables = new HashSet<>();
    private final Set<String> listVariables = new HashSet<>();

    public Environment() {
        defineVariables();
    }

    public void defineVariables() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            numberVariables.add("" + alphabet.charAt(i));
            values.put("" + alphabet.charAt(i), 0.0);
        }

        listVariables.add("L₁");
        listVariables.add("L₂");
        listVariables.add("L₃");
        listVariables.add("L₄");
        listVariables.add("L₅");
        listVariables.add("L₆");
        List<Double> list = new ArrayList<>();
        list.add(1.0);
        for (String string : listVariables) {
            values.put(string, new TiList(list));
        }
    }

    public Object get(Token name) {
        if (values.containsKey(name.lexeme)) return values.get(name.lexeme);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    public Object getListIndex(Token name, Object index) {
        if (!(index instanceof Double)) throw new RuntimeError(name, "Index must be a number.");
        double indexNumber = (double)index;
        TiList list = ((TiList) values.get(name.lexeme));
        if (indexNumber <= 0 || indexNumber > list.size()) {
            throw new RuntimeError(name, "Index is out of range for this list.");
        }

        return list.get(indexNumber);
    }

    public void assign(Token name, Object value) {
        if (numberVariables.contains(name.lexeme)) {
            if (!(value instanceof Double)) throw new RuntimeError(name, "Cannot assign a non-number to a number variable.");
        }

        if (listVariables.contains(name.lexeme)) {
            if (!(value instanceof TiList)) throw new RuntimeError(name, "Cannot assign a non-list to a list variable.");
        }

        values.put(name.lexeme, value);
    }

    public void assignListIndex(Token name, Object value, Object index) {
        if (!(value instanceof Double)) throw new RuntimeError(name, "Cannot assign a non-number to a list element.");
        if (!(index instanceof Double)) throw new RuntimeError(name, "Index must be a number.");

        double indexNumber = (double)index;
        TiList list = ((TiList) values.get(name.lexeme));
        if (indexNumber <= 0 || indexNumber > list.size() + 1) {
            throw new RuntimeError(name, "Index is out of range for this list.");
        }

        if (indexNumber == list.size() + 1) {
            list.add((double)value);
        } else {
            list.setIndex(indexNumber, (double)value);
        }
    }

}