package com.matrixhero.ollama.client.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a tool that can be used by the model.
 * This class defines the structure and capabilities of a tool that can be invoked during model interactions.
 */
public class Tool {
    /**
     * The name of the tool.
     * This should be a unique identifier for the tool.
     */
    private String name;

    /**
     * The description of what the tool does.
     * This helps the model understand when and how to use the tool.
     */
    private String description;

    /**
     * The parameters that the tool accepts.
     * This defines the input structure required to use the tool.
     */
    private Parameters parameters;

    /**
     * Inner class that defines the structure of tool parameters.
     */
    public static class Parameters {
        /**
         * The type of the parameters object.
         * This is typically "object" for structured parameters.
         */
        private String type;

        /**
         * The properties of the parameters.
         * This defines the individual fields that can be passed to the tool.
         */
        private Properties properties;

        /**
         * The required fields for the parameters.
         * This specifies which properties must be provided when using the tool.
         */
        private List<String> required;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<String> getRequired() { return required; }
        public void setRequired(List<String> required) { this.required = required; }

        public Map<String, Property> getProperties() { return properties.getProperties(); }
        public void setProperties(Map<String, Property> properties) { this.properties.setProperties(properties); }
    }

    /**
     * Inner class that defines the properties of tool parameters.
     */
    public static class Properties {
        /**
         * The properties of the parameters.
         * This defines the individual fields that can be passed to the tool.
         */
        private Map<String, Property> properties;

        public Map<String, Property> getProperties() { return properties; }
        public void setProperties(Map<String, Property> properties) { this.properties = properties; }
    }

    /**
     * Inner class that defines the structure of a property.
     */
    public static class Property {
        /**
         * The type of the property.
         * This can be "string", "object", etc.
         */
        private String type;

        /**
         * The description of the property.
         * This helps explain what the property is used for.
         */
        private String description;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Parameters getParameters() { return parameters; }
    public void setParameters(Parameters parameters) { this.parameters = parameters; }
} 