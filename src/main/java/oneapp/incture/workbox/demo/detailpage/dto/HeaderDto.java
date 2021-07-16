package oneapp.incture.workbox.demo.detailpage.dto;

public class HeaderDto {
		
		private String type;
		private String value;
		private String name;
		private String key;
		
		public String getType() {
			return type;
		}

		public HeaderDto(String type, String name, String key,String value) {
			super();
			this.type = type;
			this.name = name;
			this.key = key;
			this.value=value;
		}
		

		

		public HeaderDto() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setType(String type) {
			this.type = type;
		}

		

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return "HeaderDto [type=" + type + ", name=" + name + ", key=" + key + "]";
		}

	}