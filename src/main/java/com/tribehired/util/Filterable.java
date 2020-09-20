package com.tribehired.util;

import java.util.function.Predicate;

public interface Filterable<T> {
	Predicate<T> getFilter();
}
