DOCS_DIR = $(ROOT_DIR)/docs
REPO = $(shell git config --get remote.origin.url)
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = 5099
COLOUR_THEME = dark-green

.PHONY: blog

blog-clean:
	@echo "\nCleaning old blog build ..."

blog-pre:
	@echo "\nBuilding blog ...\n"

blog-css:
	@lessc less/styles-$(COLOUR_THEME).less docs/css/styles.css

blog-clojure:
	@echo 'Add a `lein` command here ...'

blog-local: blog-pre blog-css blog-clojure

blog: blog-clean blog-local

blog-dev: blog
	@echo "\nRunning blog server on http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)..."
	@lein simpleton $(LOCAL_DOCS_PORT) file :from $(DOCS_DIR)
