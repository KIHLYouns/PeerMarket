#!/bin/bash

# Output file
output_file="code.txt"

# Clear the output file if it already exists
> "$output_file"

# Find all .java and .fxml files inside the src directory and its subdirectories
find src -type f \( -name "*Dto.java" -o -name "*.test" \) | while read -r file; do
    # Append the file path as a header
    echo "// File: $file" >> "$output_file"
    # Append the file content
    cat "$file" >> "$output_file"
    # Add a separator between files
    echo -e "\n\n" >> "$output_file"
done

echo "All files inside 'src' have been combined into $output_file"