DOCS_DIR = $(ROOT_DIR)/docs
REPO = $(shell git config --get remote.origin.url)
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = 5099

.PHONY: blog

clean-blog:
	@echo "\nCleaning old blog build ..."

pre-blog:
	@echo "\nBuilding blog ...\n"

clojure-blog:
	@echo 'Add a `lein` command here ...'

local-blog: pre-blog clojure-blog

blog: clean-blog local-blog

devblog: blog
	@echo "\nRunning blog server on http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)..."
	@lein simpleton $(LOCAL_DOCS_PORT) file :from $(DOCS_DIR)
