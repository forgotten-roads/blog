DOCS_DIR = $(ROOT_DIR)/docs
CSS_DIR = $(DOCS_DIR)/css
REPO = $(shell git config --get remote.origin.url)
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = $(lastword $(shell grep dev-port project.clj))
LESS_DIR = src/less
COLOUR_THEME = frmx
AWS_BUCKET = forgotten.roads.mx/blog

blog: blog-clean blog-local

blog-clean:
	@echo "\nCleaning old blog build ..."

blog-pre:
	@echo "\nBuilding blog ...\n"

blog-css:
	@lessc $(LESS_DIR)/styles-$(COLOUR_THEME).less $(CSS_DIR)/styles.css

blog-clojure:
	@frmx gen
	@echo

blog-local: blog-pre blog-css blog-clojure

blog-dev-gen: blog
	@echo "\nRunning blog server from generated static content ..."
	@echo "URL: http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)"
	@lein simpleton $(LOCAL_DOCS_PORT) file :from $(DOCS_DIR)

blog-dev:
	@echo "\nRunning blog server from code ..."
	@echo "URL: http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)"
	@frmx run

.PHONY: blog

publish-prep:
	cp resources/site-verification/* docs/
	cp resources/sitemaps/* docs/

publish-aws-all: publish-prep
	@aws --profile=frmx s3 cp docs/ s3://$(AWS_BUCKET)/ --recursive

publish-aws: publish-prep
	@for f in `git status|grep modified|awk '{print $$2}'|egrep '^docs/'` ; do \
		aws --profile=frmx s3 \
			cp "$$f" s3://$(AWS_BUCKET)`echo $$f|sed -e 's/^docs\///'` ; \
	done
