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

Or, more explicitely:

```bash
$ make blog
```


## Checking Metadata and Content

TBD


## Publishing Content

TBD
