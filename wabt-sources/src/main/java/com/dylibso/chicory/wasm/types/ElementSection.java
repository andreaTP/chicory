/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ElementSection
extends Section {
    private final List<Element> elements;

    private ElementSection(List<Element> elements) {
        super(9L);
        this.elements = List.copyOf(elements);
    }

    public Element[] elements() {
        return this.elements.toArray(new Element[0]);
    }

    public int elementCount() {
        return this.elements.size();
    }

    public Element getElement(int idx) {
        return this.elements.get(idx);
    }

    public Stream<Element> stream() {
        return this.elements.stream();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ElementSection)) {
            return false;
        }
        ElementSection that = (ElementSection)o;
        return Objects.equals(this.elements, that.elements);
    }

    public int hashCode() {
        return Objects.hashCode(this.elements);
    }

    public static final class Builder {
        private final List<Element> elements = new ArrayList<Element>();

        public Builder addElement(Element element) {
            Objects.requireNonNull(element, "element");
            this.elements.add(element);
            return this;
        }

        public ElementSection build() {
            return new ElementSection(this.elements);
        }
    }
}

