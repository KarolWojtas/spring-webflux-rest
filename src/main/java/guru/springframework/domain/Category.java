package guru.springframework.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="category_shop")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@Id
	private String id;
	private String description;

}
