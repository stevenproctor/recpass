# recpass

FIXME: my new application.

## Installation

Download from https://github.com/stevenproctor/recpass.

## Usage

This application Requires a GiantBomb API key.

![Giant Bomb](https://upload.wikimedia.org/wikipedia/en/4/4b/Giant_Bomb_logo.png)

You can get started by signing up for an API key [here](https://www.giantbomb.com/api/).

Once you're logged in, go back to the [API page](https://www.giantbomb.com/api/) to access your key.

The key is provided by an Environment Variable `GIANT_BOMB_API_KEY` that is used by the server.

Run the project directly:

    $ clojure -m stevenproctor.recpass

or

    $ make

Build an uberjar:

    $ clojure -A:uberjar

Run that uberjar:

    $ java -jar recpass.jar

## Options

    -p, --port PORT  3456  Port number
    -h, --help

## License

Copyright © 2023 Proctor

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
