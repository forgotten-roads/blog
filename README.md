# blog.forgotten.roads.mx

*The Blog of Forgotten Roads MX*


## Prerequisites

```bash
$ make setup
```

Set the `PATH` to include the project's executable:

```bash
export PATH=$PATH:`pwd`/bin
```


## Creating Post Stubs

```bash
$ frmx new post md
```

or, for example,

```bash
$ frmx new post html
```

For more options see `frmx new post help`.


## Generating Static Files

```bash
$ frmx gen
```

If you'd like to run a dev web server with the generated content served at the
doc root, you can use this `make` target:

```
$ make blog-dev-generated
```


## Checking Metadata and Content

TBD


## Publishing Content

TBD
