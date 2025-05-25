# ViSiMIPS Makefile
# Builds, tests, and runs the MIPS pipeline simulator

# Directories
SRC_DIR = src
TEST_DIR = test
BUILD_DIR = build
LIB_DIR = lib
CLASSES_DIR = $(BUILD_DIR)/classes
TEST_CLASSES_DIR = $(BUILD_DIR)/test-classes

# Dependencies
JUNIT_VERSION = 4.13.2
HAMCREST_VERSION = 1.3
JUNIT_JAR = $(LIB_DIR)/junit-$(JUNIT_VERSION).jar
HAMCREST_JAR = $(LIB_DIR)/hamcrest-core-$(HAMCREST_VERSION).jar

# Java settings
JAVA_FILES = $(shell find $(SRC_DIR) -name "*.java")
TEST_FILES = $(shell find $(TEST_DIR) -name "*.java" 2>/dev/null || true)
MAIN_CLASS = assembler.Main

# Default target
.PHONY: all
all: dependencies compile test

# Download dependencies
.PHONY: dependencies
dependencies: $(JUNIT_JAR) $(HAMCREST_JAR)

$(LIB_DIR):
	mkdir -p $(LIB_DIR)

$(JUNIT_JAR): | $(LIB_DIR)
	@echo "Downloading JUnit $(JUNIT_VERSION)..."
	@if command -v curl >/dev/null 2>&1; then \
		curl -L -o $(JUNIT_JAR) "https://repo1.maven.org/maven2/junit/junit/$(JUNIT_VERSION)/junit-$(JUNIT_VERSION).jar"; \
	elif command -v wget >/dev/null 2>&1; then \
		wget -O $(JUNIT_JAR) "https://repo1.maven.org/maven2/junit/junit/$(JUNIT_VERSION)/junit-$(JUNIT_VERSION).jar"; \
	else \
		echo "Error: Neither curl nor wget found. Please install one of them."; \
		exit 1; \
	fi

$(HAMCREST_JAR): | $(LIB_DIR)
	@echo "Downloading Hamcrest Core $(HAMCREST_VERSION)..."
	@if command -v curl >/dev/null 2>&1; then \
		curl -L -o $(HAMCREST_JAR) "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/$(HAMCREST_VERSION)/hamcrest-core-$(HAMCREST_VERSION).jar"; \
	elif command -v wget >/dev/null 2>&1; then \
		wget -O $(HAMCREST_JAR) "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/$(HAMCREST_VERSION)/hamcrest-core-$(HAMCREST_VERSION).jar"; \
	else \
		echo "Error: Neither curl nor wget found. Please install one of them."; \
		exit 1; \
	fi

# Compile source files
.PHONY: compile
compile: $(CLASSES_DIR)
	@echo "Compiling source files..."
	@javac -cp $(CLASSES_DIR) -d $(CLASSES_DIR) $(JAVA_FILES)
	@echo "Source compilation complete."

$(CLASSES_DIR):
	mkdir -p $(CLASSES_DIR)

# Compile and run tests
.PHONY: test
test: compile
ifneq ($(TEST_FILES),)
	@echo "Compiling test files..."
	@mkdir -p $(TEST_CLASSES_DIR)
	@javac -cp $(CLASSES_DIR):$(JUNIT_JAR):$(HAMCREST_JAR) -d $(TEST_CLASSES_DIR) $(TEST_FILES)
	@echo "Test compilation complete."
	@echo "Running all tests..."
	@java -cp $(CLASSES_DIR):$(TEST_CLASSES_DIR):$(JUNIT_JAR):$(HAMCREST_JAR) org.junit.runner.JUnitCore \
		assembler.ALUTest \
		assembler.WBTest \
		assembler.MEMTest \
		assembler.MEM_WB_PipelineTest \
		assembler.IF_ID_PipelineTest
	@echo "Test run complete!"
else
	@echo "No test files found. Skipping tests."
endif

# Run the application
.PHONY: run
run: compile
	@echo "Starting ViSiMIPS..."
	@java -cp $(CLASSES_DIR) $(MAIN_CLASS)

# Run with debug flags (for compatibility issues)
.PHONY: run-debug
run-debug: compile
	@echo "Starting ViSiMIPS with debug flags..."
	@java -cp $(CLASSES_DIR) \
		-Djava.awt.headless=false \
		-Dapple.awt.application.name=ViSiMIPS \
		-Dswing.aatext=true \
		$(MAIN_CLASS)

# Clean build artifacts
.PHONY: clean
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf $(BUILD_DIR)
	@echo "Clean complete."

# Clean everything including dependencies
.PHONY: clean-all
clean-all: clean
	@echo "Cleaning dependencies..."
	@rm -rf $(LIB_DIR)
	@echo "Clean all complete."

# Help
.PHONY: help
help:
	@echo "ViSiMIPS Makefile Commands:"
	@echo "  make          - Download dependencies, compile, and run tests"
	@echo "  make run      - Run the ViSiMIPS application"
	@echo "  make run-debug- Run with compatibility debug flags"
	@echo "  make test     - Run all tests"
	@echo "  make compile  - Compile source files only"
	@echo "  make clean    - Clean build artifacts"
	@echo "  make clean-all- Clean everything including dependencies"
	@echo "  make help     - Show this help message" 