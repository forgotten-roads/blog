PROJ = forgotten-roads.github.io
NAME = blog
ROOT_DIR = $(shell pwd)

include dev-resource/make/code.mk
include dev-resource/make/publish.mk
include dev-resource/make/test.mk
include dev-resource/make/git.mk
