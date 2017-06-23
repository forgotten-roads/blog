BLOG_DIR = $(ROOT_DIR)/blog
CSS_DIR = $(BLOG_DIR)/css
REPO = $(shell git config --get remote.origin.url)
LOCAL_BLOG_HOST = localhost
LOCAL_BLOG_PORT = $(lastword $(shell grep dev-port project.clj))
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
	@echo "URL: http://$(LOCAL_BLOG_HOST):$(LOCAL_BLOG_PORT)/blog"
	@lein simpleton $(LOCAL_BLOG_PORT) file :from $(ROOT_DIR)

blog-dev:
	@echo "\nRunning blog server from code ..."
	@echo "URL: http://$(LOCAL_BLOG_HOST):$(LOCAL_BLOG_PORT)/blog"
	@frmx run

.PHONY: blog

publish-prep:
	cp resources/site-verification/* blog/
	cp resources/sitemaps/* blog/

publish-aws-all: publish-prep
	@aws --profile=frmx s3 cp blog/ s3://$(AWS_BUCKET)/ --recursive

publish-aws: publish-prep
	@for f in `git status|grep modified|awk '{print $$2}'|egrep '^blog/'` ; do \
		aws --profile=frmx s3 \
			cp "$$f" s3://$(AWS_BUCKET)`echo $$f|sed -e 's/^blog\///'` ; \
	done
