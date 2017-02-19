DOCS_DIR = $(ROOT_DIR)/docs
REPO = $(shell git config --get remote.origin.url)
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = 5099
#COLOUR_THEME = dark-green
COLOUR_THEME = elegantblue

.PHONY: blog

blog-clean:
	@echo "\nCleaning old blog build ..."

blog-pre:
	@echo "\nBuilding blog ...\n"

blog-css:
	@lessc less/styles-$(COLOUR_THEME).less docs/css/styles.css

blog-clojure:
	@frmx gen

blog-local: blog-pre blog-css blog-clojure

blog: blog-clean blog-local

blog-dev-generated: blog
	@echo "\nRunning blog server from generated static content ..."
	@echo "URL: http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)"
	@lein simpleton $(LOCAL_DOCS_PORT) file :from $(DOCS_DIR)

blog-dev:
	@echo "\nRunning blog server from code ..."
	@echo "URL: http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)"
	@lein ring server-headless $(LOCAL_DOCS_PORT)
