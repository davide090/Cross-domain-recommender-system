package uniroma3.it.sii.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;


public class Configuration implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private static Configuration configInstance;
    private String propertiesConfigFilePath = "/config/config.properties";


    private String likesPath;
    private String wikidataPath;
    private String similarityPath;
    private String model1_wordvector_url;
    private String model2_wordvector_url;
    private String model1_similarity_url;
    private String model2_similarity_url;
    
    private String resourcePath; //cartella resources

  

    private Configuration() {
        Properties prop = new Properties();
        try (InputStream input = Configuration.class.getResourceAsStream(propertiesConfigFilePath)) {
           
        	prop.load(input);
            this.resourcePath = prop.getProperty("resource.path");
            this.likesPath = this.resourcePath + "likes/";
            this.wikidataPath = this.resourcePath + "wikidata/";
            this.similarityPath = this.resourcePath + "similarity/";
            this.model1_wordvector_url = prop.getProperty("model1_wordvector.url");
            this.model2_wordvector_url = prop.getProperty("model2_wordvector.url");
            this.model1_similarity_url = prop.getProperty("model1_similarity.url");
            this.model2_similarity_url = prop.getProperty("model2_similarity.url");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Return the singleton object of this Factory.
     *
     * @return Singleton
     */
    public static synchronized Configuration getInstance() {
        if (configInstance == null) {
            configInstance = new Configuration();
        }
        return configInstance;
    }

  
    public String getPropertiesConfigFilePath() {
        return propertiesConfigFilePath;
    }
    
	public String getResourcePath() {
		return resourcePath;
	}

	public String getLikesPath() {
		return likesPath;
	}

	public String getWikidataPath() {
		return wikidataPath;
	}

	public String getSimilarityPath() {
		return similarityPath;
	}

	public String getModel1_wordvector_url() {
		return model1_wordvector_url;
	}

	public String getModel2_wordvector_url() {
		return model2_wordvector_url;
	}

	public String getModel1_similarity_url() {
		return model1_similarity_url;
	}

	public String getModel2_similarity_url() {
		return model2_similarity_url;
	}
    
    
	
}