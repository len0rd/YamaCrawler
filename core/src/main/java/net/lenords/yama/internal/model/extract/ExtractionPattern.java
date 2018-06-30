package net.lenords.yama.internal.model.extract;


import java.util.List;
import java.util.Map;

/**
 * Interface for extraction methods
 *
 * @author len0rd
 * @since 2018-03-21
 */
public interface ExtractionPattern<T> {

	T build();

	ExtractionResult buildAndExecute(T matchAgainst);

	ExtractionResult execute(T matchAgainst);

	List<String> getExtractorNamesToRetrieve();

	String getName();

}
