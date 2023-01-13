# Changes to the ACA PY OpenAPI file

svc-aca-py.json is the file prov
See the original version on [hyperledger/aries-cloudagent-python](https://github.com/hyperledger/aries-cloudagent-python/blob/main/open-api/openapi.json).

Our version contains some custom changes to account to ensure compatibility with VCMS. These need to be considered when upgrading the file to newer versions.

1. `AttachDecoratorDataJWS` is renamed to `AttachDecoratorDataJws`. This is necessary for the Java client code generation to
   succeed.
2. V10PresentationExchange.presentation is customized, so the revealed attributes are easier to access.
It should be the following. Use the following snippet.

This is discussed in https://github.com/hyperledger/aries-cloudagent-python/issues/1976.
V10PresentationExchange presentation fields should be of type IndyProof.
IndyProof requested_proof should be IndyProofRequestedProof, not IndyProof_requested_proof.

```json
"presentation": {
	"description": "(Indy) presentation (also known as proof)",
	"type": "object",
	"properties": {
		"proof": {
			"type": "object"
		},
		"identifiers": {
			"type": "object"
		},
		"requested_proof": {
			"type": "object",
			"properties": {
				"revealed_attrs": {
					"type": "object",
					"additionalProperties": {
						"type": "object",
						"properties": {
							"raw": {
								"type": "string"
							},
							"encoded": {
								"type": "string"
							},
							"sub_proof_index": {
								"type": "integer"
							}
						}
					}
				},
				"self_attested_attrs": {
					"type": "object"
				},
				"unrevealed_attrs": {
					"type": "object"
				},
				"predicates": {
					"type": "object"
				}
			}
		}
	}
},
```

3. IndyProofReqAttrSpec can be requested either with the name(single attribute) or names(grouped attribute request). Only one
   of these fields must be specified. and until non_null inclusion is fixed in the project we modify the API and remove the
   names attribute from our models.
4. V10PresentationExchange is missing `auto_verify` field, which is actually used by acapy.

```json
"auto_verify" : {
	"type" : "boolean",
	"example" : false,
	"description" : "Verifier choice to auto-verify proof presentation"
},
```
