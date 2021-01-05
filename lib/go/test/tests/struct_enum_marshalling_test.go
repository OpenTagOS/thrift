package tests

import (
	"encoding/json"
	"structenummarshallingtest"
	"testing"
)

func TestUnmarshaling(t *testing.T) {
	dummyStruct := &structenummarshallingtest.Dummy{}

	err := json.Unmarshal([]byte(`{"dummyEnum": "FIRST"}`), dummyStruct)
	if err != nil {
		t.Error(err)
	}

	if dummyStruct.DummyEnum != structenummarshallingtest.DummyEnum_FIRST {
		t.Errorf(
			"enums are not equals. actual: %d. expected: %d",
			dummyStruct.DummyEnum,
			structenummarshallingtest.DummyEnum_FIRST,
		)
	}
}

func TestMarshaling(t *testing.T) {
	dummyStruct := &structenummarshallingtest.Dummy{
		DummyEnum: structenummarshallingtest.DummyEnum_FIRST,
	}

	jsonBytes, err := json.Marshal(dummyStruct)
	if err != nil {
		t.Error(err)
	}

	expectedJson := `{"dummyEnum":"FIRST"}`
	actualJson := string(jsonBytes)

	if actualJson != expectedJson {
		t.Errorf(
			`json strings are not equal. expected: "%s". actual: "%s"`,
			expectedJson,
			actualJson,
		)
	}
}
