DOCS_DIR = $(ROOT_DIR)/blog
REPO = $(shell git config --get remote.origin.url)
DOCS_BUILD_DIR = $(DOCS_DIR)/build
DOCS_PROD_DIR = $(DOCS_DIR)/master
DOCS_GIT_HACK = $(DOCS_DIR)/.git
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = 5099

.PHONY: blog

$(DOCS_GIT_HACK):
	-@ln -s $(ROOT_DIR)/.git $(DOCS_DIR)

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
	@lein simpleton $(LOCAL_DOCS_PORT) file :from $(DOCS_PROD_DIR)

prod-blog: clean-blog $(DOCS_GIT_HACK) local-blog

setup-temp-repo: $(DOCS_GIT_HACK)
	@echo "\nSetting up temporary git repos for gh-pages ...\n"
	@rm -rf $(DOCS_PROD_DIR)/.git $(DOCS_PROD_DIR)/*/.git
	@cd $(DOCS_PROD_DIR) && git init
	@cd $(DOCS_PROD_DIR) && git add * > /dev/null
	@cd $(DOCS_PROD_DIR) && git commit -a -m "Generated content." > /dev/null

teardown-temp-repo:
	@echo "\nTearing down temporary gh-pages repos ..."
	@rm $(DOCS_DIR)/.git
	@rm -rf $(DOCS_PROD_DIR)/.git $(DOCS_PROD_DIR)/*/.git

publish-blog: prod-blog setup-temp-repo
	@echo "\nPublishing blog ...\n"
	@cd $(DOCS_PROD_DIR) && git push -f $(REPO) master:gh-pages
	@make teardown-temp-repo
