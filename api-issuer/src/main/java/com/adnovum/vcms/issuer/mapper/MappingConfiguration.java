package com.adnovum.vcms.issuer.mapper;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

import static org.modelmapper.config.Configuration.AccessLevel.PROTECTED;

@Configuration
public class MappingConfiguration {

	@Bean
	ModelMapper modelMapper(List<MappingConfigurationProvider> configurationProviders) {
		var modelMapper = new ModelMapper();

		modelMapper.addConverter(new UUIDToStringConverter());

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration()
				.setFieldMatchingEnabled(true)
				.setFieldAccessLevel(PROTECTED);

		configurationProviders.forEach(provider -> provider.configure(modelMapper));

		return modelMapper;
	}

	private static final class UUIDToStringConverter extends AbstractConverter<UUID, String> {
		@Override
		protected String convert(UUID source) {
			return source == null ? null :source.toString();
		}
	}
}
